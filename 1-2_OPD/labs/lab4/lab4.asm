org 0x106

start: cla
st res
ld y
push
call 0x731
pop
sub res
st res
ld x
push
call 0x731
pop
dec
add res
st res
ld z
push
call 0x731
pop
sub res
st res
hlt

z: word 0x6522
y: word 0xffa6
x: word 0xd314
res: word 0x0 

org 0x731
ld &1
bpl ld_const
sub const1
bmi ld_const
beq ld_const
add const1
add &0x1
add &0x1
add const2
jump save
ld_const: ld const1
save: st &1
ret

const1: word 0xf424
const2: word 0x26