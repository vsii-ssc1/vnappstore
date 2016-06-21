//
//  AppPlugin.m
//  vnappstore
//
//  Created by Dam Than Siem on 6/21/16.
//
//

#import "AppPlugin.h"

@implementation AppPlugin
// do nothing and return failure alway
- (void)open:(CDVInvokedUrlCommand *)command{
        NSString* schemaApp = [command.arguments objectAtIndex:0];
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:schemaApp]];
    
}

// check whether an app is installed or not by URL Schemes
- (void)check:(CDVInvokedUrlCommand *)command{
    
    CDVPluginResult *pluginResult = nil;
    
    NSString* schemaApp = [command.arguments objectAtIndex:0];
    
    if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:schemaApp]]) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"{\"success\": true}"];
        
    }else{
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"{\"success\": false}"];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    
}

@end
