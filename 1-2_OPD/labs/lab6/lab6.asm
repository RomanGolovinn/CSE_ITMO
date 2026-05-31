org 0x0
v0: word $default, 0x180
v1: word $default, 0x180
v2: word $vu2, 0x180
v3: word $vu3, 0x180
v4: word $default, 0x180
v5: word $default, 0x180
v6: word $default, 0x180
v7: word $default, 0x180

default: iret

org 0x21
x: word 0x0

start:
di
cla
ld #0x0a
out 5
ld #0x0b
out 7
ei

main_loop:
ld x
dec
st tmp
cmp c_max_127
bpl main_out_odz
cmp c_min_128
blt main_out_odz
ld tmp
st x
jump main_loop

main_out_odz:
ld c_max_127
st x
jump main_loop

vu2:
push
in 4
and x
not
sxtb 
st x
pop
iret

vu3:
push
ld x
asl
asl
neg
sub c_7
st tmp
cmp c_max_127
bpl vu3_out_odz
ld tmp
cmp c_min_128
blt vu3_out_odz
ld tmp
jump vu3_save

vu3_out_odz:
ld c_max_127

vu3_save:
st x
out 6
pop
iret

tmp: word 0x0
c_min_128: word 0xff80
c_max_127: word 0x7f
c_7: word 0x7

