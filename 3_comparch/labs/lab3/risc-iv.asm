.data
.org 0x88

input_addr:       .word 0x80
output_addr:      .word 0x84
mask_hi:          .word 0xFF00FF00
mask_lo:          .word 0x00FF00FF
byte_shift:       .word 8
halfword_shift:   .word 16
stack_step:       .word 4

.text
_start:
    lui t0, %hi(input_addr)
    addi t0, t0, %lo(input_addr)
    lw t0, 0(t0)
    lw a0, 0(t0)

    lui t0, %hi(stack_top)
    addi sp, t0, %lo(stack_top)
    jal ra, reverse32

    lui t0, %hi(output_addr)
    addi t0, t0, %lo(output_addr)
    lw t0, 0(t0)
    sw a0, 0(t0)
    halt

; reverse32(n): reverse order of the four bytes in n.
reverse32:
    lui t3, %hi(stack_step)
    addi t3, t3, %lo(stack_step)
    lw t3, 0(t3)
    add sp, sp, t3
    sw ra, 0(sp)

    jal ra, swap_bytes_in_pairs

    lui t2, %hi(halfword_shift)
    addi t2, t2, %lo(halfword_shift)
    lw t2, 0(t2)
    sll t0, a0, t2
    srl t1, a0, t2
    or a0, t0, t1

    lw ra, 0(sp)
    lui t3, %hi(stack_step)
    addi t3, t3, %lo(stack_step)
    lw t3, 0(t3)
    sub sp, sp, t3
    jalr zero, ra, 0

; Nested helper: swap adjacent bytes, independently in each halfword.
swap_bytes_in_pairs:
    lui t3, %hi(stack_step)
    addi t3, t3, %lo(stack_step)
    lw t3, 0(t3)
    add sp, sp, t3
    sw ra, 0(sp)

    lui t2, %hi(byte_shift)
    addi t2, t2, %lo(byte_shift)
    lw t2, 0(t2)
    sll t0, a0, t2
    lui t2, %hi(mask_hi)
    addi t2, t2, %lo(mask_hi)
    lw t2, 0(t2)
    and t0, t0, t2

    lui t2, %hi(byte_shift)
    addi t2, t2, %lo(byte_shift)
    lw t2, 0(t2)
    srl t1, a0, t2
    lui t2, %hi(mask_lo)
    addi t2, t2, %lo(mask_lo)
    lw t2, 0(t2)
    and t1, t1, t2

    or a0, t0, t1

    lw ra, 0(sp)
    lui t3, %hi(stack_step)
    addi t3, t3, %lo(stack_step)
    lw t3, 0(t3)
    sub sp, sp, t3
    jalr zero, ra, 0

stack_top:
