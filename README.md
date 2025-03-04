# TraditioGL

Bringing back the deprecated Fixed-Function Pipeline to modern OpenGL.

# Table of contents

* [What is the Fixed-Function Pipeline?](#what)
* [What is the goal of TraditioGL?](#goal)

<div id="what"></div>

## What is the Fixed-Function Pipeline?

The [Fixed-Function Pipeline](https://www.khronos.org/opengl/wiki/Fixed_Function_Pipeline) (FFP) is the legacy rendering model in OpenGL (before OpenGL 3.0). 
It provided a built-in way to render 3D graphics without using shaders, but it was replaced by the Programmable Pipeline in modern OpenGL.

<div id="goal"></div>

## What is the goal of TraditioGL?

TraditioGL intends to bring back the general configurable state of the deprecated Fixed-Function Pipeline using the programmable state of modern OpenGL.
The project provides an internal layer, opaque to the user, to perform vertex processing, fragment shading, texture sampling, through the use of Shaders, Vertex Buffer Object, Framebuffer Object, etc.
For example :
* Drawing geometries with Immediate Mode as been replaced by an underlying VertexProcessor which packs the vertex data into a Vertex Buffer Object.
* Transformations Matrices are maintained using a stack on the CPU and applied to each vertex in a Shader.
* Texture sampling and fog equations are performed in a Shader.