# Le rendu final est le main

# 2024 Project : _Chromat-Ynk_ CY Tech
## Table of Contents
- [Introduction](#Introduction)
- [Description](#Description)
- [Progress](#Progress)
- [Current Class Diagram](#Current_Class_Diagram)
- [Current_Use_Case_Diagram](#Current_Use_Case_Diagram)
- [Members](#Members)

## Introduction
This project aims to create a Java program for drawing arbitrary shapes on a screen using simple instructions. Users will be able to specify actions such as moving forward by a certain number of pixels, rotating by a certain angle, changing the thickness and color of drawn lines, etc.

Features: Real-time drawing of shapes on a screen. Definition and execution of a dedicated drawing instruction language. Dynamic modification of line thickness and color. Saving and loading of creations for future use.

The specifications can be found in the discussion tab -> tasklist, it will be updated with each project progress and change in requirements.

## Description
### Graphical user interface
First of all, we will need to create the graphical user interface in which we have the canvas where the drawing will be drawn, this one will take the biggest part of the window, next to it will be present a section to write lines of instruction that will be specified in the command description. This section will include the possibility to import a text file of instruction, and in that case, there's the possibility to chose the speed at which the drawing will be drawn, it will be implemented by a slider, also a step by step button allowing to draw an instruction by each click on the specified button. 

Along the drawing, there'll be an erase button to clean the GUI, therefore an saving button will be included next to it, allowing us to save the drawing if there's one as an image. In case of a command ERROR, the user will have the choice to either skip it and continue the drawing, the type of error will be printed at the end, either chose to stop the file execution at the ERROR instruction, if it is a written one, the execution process won't be launched. 

In order to draw in the GUI we'll need cursor to pinpoint the drawing point, it can be a triangle for example.

### Interpreter
In order for the command to draw the action instruction on the GUI, we will need an interpreter that will take the command and act consequently, it will be composed of a LEXER, which will convert the entry instruction into a sequence of tokens, a PARSER, which will produce a syntax tree along a grammar formality from the tokens and an INTERPRETER, which will take care of drawing the instruction on the GUI.

### Command langage definition
The instructions bloc will be composed of simples instructions, it may be a chain of instructions where there'll be the possibility to use condition bloc such as IF, WHILE, FOR and also mirror method that will duplicate the drawing cursor and follow the original instruction, they'll be defined accordingly.

The use of variable will be possible in order to use condition bloc since some instructions should allow the creation of variables and retrieval of properties from cursors. All instructions that handle values can use a variable instead of a value. The value of the variable will then be used., they also need to be defined according to their type. 

Simple instruction are as follow : 

• FWD value [%]: Moves the cursor forward in a relative manner, expressed in pixels or as a percentage of the largest dimension of the drawing area. In both cases, the value is a real number.

• BWD value [%]: Moves the cursor backward in a relative manner.

• TURN value: Rotates the cursor in a relative manner, expressed in degrees.

• MOV x [%], y [%]: Moves the cursor relative to its current position (in pixels or %).

• POS x [%], y [%]: Positions the cursor on the screen (in pixels or %).

• HIDE: Hides the cursor on the screen.

• SHOW: Displays the cursor on the screen.

• PRESS value [%]: Indicates the pressure with which the cursor draws the shape. A real value between 0 and 1 is required, except with the % sign, where a real value between 0 and 100 is requested. For a value of 0%, nothing is drawn; for 100%, the color is opaque. Any value in between draws the shape with transparency proportional to the value.

• COLOR #RRGGBB: Determines the color of the next stroke in Web format with 6 hexadecimal characters.

• COLOR red, green, blue: Determines the color with 3 integer values between 0 and 255 or 3 real values between 0 and 1.
The interpreter must automatically detect the format of the values.

• THICK value: Sets the thickness of a stroke before moving the cursor.

• LOOKAT cursorID: Rotates the current cursor in the direction of another cursor whose identifier has been provided.

• LOOKAT x [%], y [%]: Rotates the current cursor in the direction of position x/y, either as real values or percentages.

Instruction bloc are as follow :

• IF boolean: Executes the block of instructions if the passed boolean condition is true. The implementation of the boolean type format is left open.

• FOR name [FROM v1] TO v2 [STEP v3]: Loop with the declaration of a variable name and the bounds v1 and v2 (v2 included or excluded, it's up to you to define) and with a step v3. The FROM and STEP fields are optional, and the default values are respectively 0 and 1. name must not be the name of an existing variable.

• WHILE boolean: Executes the block of instructions as long as the boolean condition is true.

• MIMIC cursorID: Creates a temporary cursor at the current cursor position and performs exactly the same actions as another cursor whose identifier has been passed.

• MIRROR x1[%], y1[%], x2[%], y2[%]: Temporarily duplicates the current cursor and performs all instructions with axial symmetry. Two points of the axis of symmetry are provided as parameters for this instruction. The values are absolute real values or percentages as for simple commands.

• MIRRORx1[%], y1[%]: Temporarily duplicates the current cursor and performs all instructions in the block with central symmetry. The symmetry point is given as a parameter for this instruction.

Variables :

• NUM name[= value]: Creates a real numeric variable. Allows for initialization of the value. If no value is provided, the default value of 0 will be used.

• STR name [= value]: Creates a string type variable. If no value is provided, an empty string is created.

• BOOL name [= value]: Creates a boolean type variable. If the value is omitted, the equivalent value of false will be used.

• DEL name: Deletes the variable with the name specified.

Every command should be interpreteable by the interpreter.


## Progress
You can find the progress in the Discussion Channel of the repository [Progress](https://github.com/bapger/Projet2024/discussions).

[07 mai 2024](https://github.com/bapger/Projet2024/discussions/10)

[14 mai 2024](https://github.com/bapger/Projet2024/discussions/14)

[15 main 2024](https://github.com/bapger/Projet2024/discussions/15#)

[16 main 2024](https://github.com/bapger/Projet2024/discussions/18)

[17 main 2024](https://github.com/bapger/Projet2024/discussions/22)

[21 main 2024](https://github.com/bapger/Projet2024/discussions/23)

[22 main 2024](https://github.com/bapger/Projet2024/discussions/24)

[23 main 2024](https://github.com/bapger/Projet2024/discussions/25)



## Members
- @clement-le-coadou  ->  Clément LE COADOU
- @kevinCYt  ->  Kevin CHEN
- @Pascalcytech  ->  Pascal KAU
- @Poupouski  ->  Hicham BETTAHAR
- @bapger  ->  Baptiste GERMAIN
