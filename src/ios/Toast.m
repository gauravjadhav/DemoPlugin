#import "Toast.h"
#import "Toast+UIView.h"
#import <Cordova/CDV.h>
#import "AFHTTPRequestOperation.h"
#import "AFHTTPRequestOperationManager.h"



typedef void (^CompletionBlock)(id object,NSError *error);



#define Host_Name @ "http://8.35.33.63:8080/Api/"

#define Upload @ "WorkOrderNotes/UpdateWorkOrderNotesFiles"



@implementation Toast

- (void)show:(CDVInvokedUrlCommand*)command {
    
  
    NSMutableDictionary *dict = [command.arguments objectAtIndex:0];
  
    NSString* documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString* foofile = [documentsPath stringByAppendingPathComponent:[dict objectForKey:@"FilePath"]];
    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:foofile];
    
    NSData *datavid = [NSData dataWithContentsOfFile:foofile];
    NSString *base64Encoded = [datavid base64EncodedStringWithOptions:0];

    
    [self upload_workorderid:[dict objectForKey:@"WorkOrderId"] notetype:[dict objectForKey:@"NoteType"] sectionid:[dict objectForKey:@"SectionId"] filename:[dict objectForKey:@"FileName"] filepath:[dict objectForKey:@"FilePath"] filesize:[dict objectForKey:@"FileSize"] fileextension:[dict objectForKey:@"FilePath"] data:base64Encoded createdby:[dict objectForKey:@"CreatedBy"] surveyorid:[dict objectForKey:@"SurveyorId"] completion:^(id object, NSError *error) {
        
        if (object)
        {
            
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"success"];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            
        }
        else
        {
            CDVPluginResult * pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"fail"];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            return;
            
        }
    }];


}


-(void)upload_workorderid:(NSString *)workorderid notetype:(NSString *)notetype sectionid: (NSString *)sectionid filename: (NSString*) filename filepath: (NSString*) filepath filesize: (NSString*)filesize fileextension:(NSString*)fileextension data:(NSString*)data createdby:(NSString*)createdby surveyorid:(NSString*)surveyorid completion:(CompletionBlock)completion
{
    NSMutableDictionary *dict = [[NSMutableDictionary alloc]init];
    [dict setObject:workorderid forKey:@"WorkOrderId"];
    [dict setObject:notetype forKey:@"NoteType"];
    [dict setObject:sectionid forKey:@"SectionId"];
    [dict setObject:filename forKey:@"FileName"];
    [dict setObject:filepath forKey:@"FilePath"];
    [dict setObject:filesize forKey:@"FileSize"];
    [dict setObject:fileextension forKey:@"FileExtension"];
    [dict setObject:data forKey:@"BinaryValue"];
    [dict setObject:createdby forKey:@"CreatedBy"];
    [dict setObject:surveyorid forKey:@"SurveyorId"];
    
    
    NSURL *url = [NSURL URLWithString:Host_Name];
    
    AFHTTPRequestOperationManager *operation = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:url];
    operation.responseSerializer = [AFJSONResponseSerializer serializer];
    
    [operation POST:Upload parameters:dict success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         completion(responseObject,nil);
     }
            failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         completion(nil,error);
     }];
}





@end