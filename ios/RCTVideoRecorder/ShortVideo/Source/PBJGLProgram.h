//
//  ShortVideoDemo
//
//  Created by 刘子文 on 16/11/28.
//  Copyright © 2016年 yuanyinguoji. All rights reserved.
//



#import <Foundation/Foundation.h>
#import <OpenGLES/ES2/gl.h>
#import <OpenGLES/ES2/glext.h>

// common attribute names
extern NSString * const PBJGLProgramAttributeVertex;
extern NSString * const PBJGLProgramAttributeTextureCoord;
extern NSString * const PBJGLProgramAttributeNormal;

// inspired by Jeff LaMarche, https://github.com/jlamarche/iOS-OpenGLES-Stuff
@interface PBJGLProgram : NSObject

- (id)initWithVertexShaderName:(NSString *)vertexShaderName fragmentShaderName:(NSString *)fragmentShaderName;

- (void)addAttribute:(NSString *)attributeName;

- (GLuint)attributeLocation:(NSString *)attributeName;
- (int)uniformLocation:(NSString *)uniformName;

- (BOOL)link;
- (void)use;

@end
