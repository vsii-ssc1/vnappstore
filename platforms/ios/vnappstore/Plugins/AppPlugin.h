//
//  AppPlugin.h
//  vnappstore
//
//  Created by Dam Than Siem on 6/21/16.
//
//

#import <Cordova/CDVPlugin.h>

@interface AppPlugin : CDVPlugin
- (void) open:(CDVInvokedUrlCommand*)command;
- (void) check:(CDVInvokedUrlCommand*)command;
@end
