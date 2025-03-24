// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

/* Pseudocode
LOOP
i = 16384
if (KBD == 0) goto WHITELOOP
if (KBD != 0) goto BLACKLOOP

WHITELOOP
if (i > 24575) goto LOOP
SCREEN[i] = 0
i = i + 1

BLACKLOOP
if (i > 24575) goto LOOP
SCREEN[i] = 1
i = i + 1
*/


@LOOP
(LOOP)
// i = 16384
@16384
D=A
@i
M=D
// if (KBD == 0) goto WHITELOOP
@KBD
D=M
@WHITELOOP
D;JEQ
// if (KBD != 0) goto BLACKLOOP
@BLACKLOOP
D;JNE
@LOOP
0;JMP

(WHITELOOP)
// if (i > 24575) goto LOOP
@i
D=M
@24575
D=D-A
@LOOP
D;JGT
// SCREEN[i] = 0
@i
A=M
M=0
// i = i + 1
@i
M=M+1
@WHITELOOP
0;JMP

(BLACKLOOP)
// if (i > 24575) goto LOOP
@i
D=M
@24575
D=D-A
@LOOP
D;JGT
// SCREEN[i] = -1
@i
A=M
M=-1
// i = i + 1
@i
M=M+1
@BLACKLOOP
0;JMP
