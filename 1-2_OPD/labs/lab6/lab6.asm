org 0x00

v0: word $default, 0x180
v1: word $default, 0x180
v2: word $vu2, 0x180
v3: word $vu3, 0x180
v4: word $default, 0x180
v5: word $default, 0x180
v6: word $default, 0x180
v7: word $default, 0x180

default:
    iret

org 0x20

x:          word 0x0000
tmp:        word 0x0000

c_min_33:   word 0xFFDF
c_max_30:   word 0x001E
c_31:       word 0x001F
c_7:        word 0x0007
c_ff:     word 255

start:
    di

    ld #0x0A
    out 5

    ld #0x0B
    out 7

    ei

main_loop:
    ld x
    dec
    call check_odz
    jump main_loop

check_odz:
    st tmp

    cmp c_31
    bge out_odz

    ld tmp
    cmp c_min_33
    blt out_odz

    ld tmp
    st x
    ret

out_odz:
    ld c_max_30
    st x
    ret

vu2:
    in 4
    and x

    not

    sxtb

    call check_odz
    iret

vu3:
    ld x

    asl
    asl

    neg

    sub c_7

    out 6
    iret