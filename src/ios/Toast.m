#import "Toast.h"
#import "Toast+UIView.h"
#import <Cordova/CDV.h>



@implementation Toast

- (void)show:(CDVInvokedUrlCommand*)command
{
    
    
    NSMutableDictionary *dictFromPhoneGap = [command.arguments objectAtIndex:0];
    
    NSString* documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString* filePath = [documentsPath stringByAppendingPathComponent:[dictFromPhoneGap objectForKey:@"FilePath"]];
    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:filePath];
    
    
    if (fileExists)
    {
        
        NSData *datavidaud = [NSData dataWithContentsOfFile:filePath];
        NSString *base64Encoded = [datavidaud base64EncodedStringWithOptions:0];
        
        NSMutableDictionary *dictToPost = [[NSMutableDictionary alloc]init];
        [dictToPost setObject:[dictFromPhoneGap objectForKey:@"WorkOrderId"] forKey:@"WorkOrderId"];
        [dictToPost setObject:[dictFromPhoneGap objectForKey:@"NoteType"]forKey:@"NoteType"];
        [dictToPost setObject:[dictFromPhoneGap objectForKey:@"SectionId"] forKey:@"SectionId"];
        [dictToPost setObject:[dictFromPhoneGap objectForKey:@"FileName"] forKey:@"FileName"];
        [dictToPost setObject:[dictFromPhoneGap objectForKey:@"FilePath"] forKey:@"FilePath"];
        [dictToPost setObject:[dictFromPhoneGap objectForKey:@"FileSize"] forKey:@"FileSize"];
        [dictToPost setObject:[dictFromPhoneGap objectForKey:@"FileExtension"] forKey:@"FileExtension"];
        [dictToPost setObject:base64Encoded forKey:@"BinaryValue"];
        [dictToPost setObject:[dictFromPhoneGap objectForKey:@"CreatedBy"] forKey:@"CreatedBy"];
        [dictToPost setObject:[dictFromPhoneGap objectForKey:@"SurveyorId"] forKey:@"SurveyorId"];
        
        
        NSError *error;
        NSData *postdata = [NSJSONSerialization dataWithJSONObject:dictToPost options:0 error:&error];
        
        NSString *serverURL = @"http://8.35.33.63:8080/Api/WorkOrderNotes/UpdateWorkOrderNotesFiles";
        
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:serverURL]];
        [request setHTTPMethod:@"POST"];
        [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
        [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [request setValue:[NSString stringWithFormat:@"%d", [postdata length]] forHTTPHeaderField:@"Content-Length"];
        [request setHTTPBody:postdata];
        NSURLConnection *conn = [[NSURLConnection alloc] initWithRequest:request delegate:self];
        
        [conn start];
        
        NSData *returnData = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
        NSDictionary *dictReponse=[NSJSONSerialization JSONObjectWithData:returnData options:NSJSONReadingMutableLeaves error:nil];
        NSLog(@"%@",dictReponse);
        
        if ([[dictReponse objectForKey:@"Message"] isEqualToString:@"Success"])
        {
            
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Success"];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            
        }
        else
        {
            
            CDVPluginResult * pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Fail"];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            
        }
        
    }
    else
    {
        
        CDVPluginResult * pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Fail"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
    
    
    
}


@end