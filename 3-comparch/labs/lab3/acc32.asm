.text
.org 0x100

_start:

    load_addr 0x80
    store_addr n
    beqz input_error       ; n == 0
    bltz input_error       ; n < 0

    load_imm 0
    store_addr steps


loop:

    load_addr n
    sub one
    beqz done

    load_addr n
    rem two
    beqz even


odd:

    load_addr n
    mul three
    bvs overflow_error     ; 3*n does not fit in 32 bits

    add one
    bvs overflow_error     ; 3*n + 1 does not fit

    store_addr n

    jmp increment_steps


even:

    load_addr n
    div two
    store_addr n

    jmp increment_steps


increment_steps:

    load_addr steps
    add one
    bvs overflow_error     ; steps does not fit in 32 bits
    store_addr steps

    jmp loop


done:

    load_addr steps
    store_addr 0x84
    halt


input_error:

    load_addr error
    store_addr 0x84
    halt


overflow_error:

    load_addr overflow
    store_addr 0x84
    halt



.data

n:          .word 0
steps:      .word 0

one:        .word 1
two:        .word 2
three:      .word 3

error:      .word -1
overflow:   .word 0xCCCCCCCC