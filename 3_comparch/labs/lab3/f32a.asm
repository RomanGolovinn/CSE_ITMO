.data
.org             0x88

output_addr:     .word  0x84
err_overflow:    .word  0xCCCCCCCC
byte_mask:       .word  0xFFFFFF00

    .text

_start:
    @p output_addr
    b!

    0x00 a!
    31 >r
init_buf_loop:
    0x5F !+
    next init_buf_loop

    print_prompt

    0x00 a!
    write_prefix

    @p 0x80
    dup 10 xor if handle_empty_name
    dup if handle_null_char
    put_byte

    20 >r
read_name_loop:
    @p 0x80
    dup 10 xor if finish_name_read
    dup if handle_null_char
    put_byte
    next read_name_loop

    @p 0x80
    dup 10 xor if finish_name_read
    dup if handle_null_char
    drop
    overflow ;

handle_empty_name:
    drop
    overflow ;

handle_null_char:
    drop
skip_rest_loop:
    @p 0x80
    dup 10 xor if finish_name_read
    drop
    skip_rest_loop ;

finish_name_read:
    drop

    33 put_byte
    0 put_byte

    0x00 a!
print_buf_loop:
    @
    255 and
    dup if done
    !b
    a 1 + a!
    print_buf_loop ;

done:
    drop
    halt

overflow:
    @p err_overflow
    !b
    halt

put_byte:
    255 and
    @
    @p byte_mask
    and
    xor
    !
    a 1 + a!
    ;

print_prompt:
    87 !b
    104 !b
    97 !b
    116 !b
    32 !b
    105 !b
    115 !b
    32 !b
    121 !b
    111 !b
    117 !b
    114 !b
    32 !b
    110 !b
    97 !b
    109 !b
    101 !b
    63 !b
    10 !b
    ;

write_prefix:
    72 put_byte
    101 put_byte
    108 put_byte
    108 put_byte
    111 put_byte
    44 put_byte
    32 put_byte
    ;