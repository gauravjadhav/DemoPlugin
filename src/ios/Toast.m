#import "Toast.h"
#import "Toast+UIView.h"
#import <Cordova/CDV.h>


@implementation Toast

- (void)show:(CDVInvokedUrlCommand*)command {
    
  
    NSMutableDictionary *dict = [command.arguments objectAtIndex:0];
    
    NSString *stringFileName = [dict objectForKey:@"FileName"];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:stringFileName];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end