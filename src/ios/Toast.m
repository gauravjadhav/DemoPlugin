#import "Toast.h"
#import "Toast+UIView.h"
#import <Cordova/CDV.h>


@implementation Toast

- (void)show:(CDVInvokedUrlCommand*)command {
    
  
    NSMutableDictionary *dict = [command.arguments objectAtIndex:0];
    

    
    NSString* documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString* foofile = [documentsPath stringByAppendingPathComponent:[dict objectForKey:@"FilePath"]];
    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:foofile];
    NSString *stringFileName;
    
    if (fileExists)
    {
        stringFileName = [NSString stringWithFormat:@"file was found with document path %@ and filepath %@",documentsPath,foofile];
        
    }
    else
    {
    
            stringFileName = [NSString stringWithFormat:@"file was not found with document path %@ and filepath %@",documentsPath,foofile];
    }
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:stringFileName];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end