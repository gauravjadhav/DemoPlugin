#import "Toast.h"
#import "Toast+UIView.h"
#import <Cordova/CDV.h>


@implementation Toast

- (void)show:(CDVInvokedUrlCommand*)command {
    
    NSString *message  = [command.arguments objectAtIndex:0];
    NSString *duration = [command.arguments objectAtIndex:1];
    NSString *position = [command.arguments objectAtIndex:2];
    
    NSMutableDictionary *dict = [command.arguments objectAtIndex:0];
    
    NSString *stringFileName = [dict objectForKey:@"FileName"];
    
    if (![position isEqual: @"top"] && ![position isEqual: @"center"] && ![position isEqual: @"bottom"]) {
        NSString *string1 = [NSString stringWithFormat:@" invalid position. valid options are 'top', 'center' and 'bottom' with filename %@", stringFileName];
        CDVPluginResult * pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:string1];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }
    
    NSInteger durationInt;
    if ([duration isEqual: @"short"]) {
        durationInt = 2;
    } else if ([duration isEqual: @"long"]) {
        durationInt = 5;
    } else {
        NSString *string2 = [NSString stringWithFormat:@" invalid position. valid options are 'top', 'center' and 'bottom' with filename %@", stringFileName];
        
        CDVPluginResult * pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:string2 ];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }
    
    [self.webView makeToast:message duration:durationInt position:position];
    NSString *string3 = [NSString stringWithFormat:@" plugin works with filename %@", stringFileName];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:string3];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end