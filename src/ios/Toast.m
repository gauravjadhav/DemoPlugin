#import "Toast.h"
#import "Toast+UIView.h"
#import <Cordova/CDV.h>



@implementation Toast

- (void)show:(CDVInvokedUrlCommand*)command
{
  
    NSMutableDictionary *dict = [command.arguments objectAtIndex:0];
  
    NSString* documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString* filePath = [documentsPath stringByAppendingPathComponent:[dict objectForKey:@"FilePath"]];
    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:filePath];
    
    NSData *datavidaud = [NSData dataWithContentsOfFile:filePath];
    NSString *base64Encoded = [datavidaud base64EncodedStringWithOptions:0];
    
    int stringlength = [base64Encoded stringlength];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:stringlength];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

}


@end