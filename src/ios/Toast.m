#import "Toast.h"
#import "Toast+UIView.h"
#import <Cordova/CDV.h>


@implementation Toast

- (void)show:(CDVInvokedUrlCommand*)command {
    
  
    NSMutableDictionary *dict = [command.arguments objectAtIndex:0];
    
    NSString *stringFileName;

    
    NSString* documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString* foofile = [documentsPath stringByAppendingPathComponent:[dict objectForKey:@"FilePath"]];
    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:foofile];
    
    if (fileExists)
    {
        stringFileName = @"file found";
        
    }
    else
    {
        stringFileName = @"file not found";
    }
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:stringFileName];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end