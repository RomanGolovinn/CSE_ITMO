.data
.org             0x88

input_addr:      .word 0x80
output_addr:     .word 0x84
err_overflow:    .word 0xCCCCCCCC
byte_mask:       .word 0xFFFFFF00

char_W:          .word 'W'
char_h:          .word 'h'
char_a:          .word 'a'
char_t:          .word 't'
char_i:          .word 'i'
char_s:          .word 's'
char_y:          .word 'y'
char_o:          .word 'o'
char_u:          .word 'u'
char_r:          .word 'r'
char_n:          .word 'n'
char_m:          .word 'm'
char_e:          .word 'e'
char_question:   .word '?'
char_H:          .word 'H'
char_l:          .word 'l'
char_comma:      .word ','
char_space:      .word ' '
char_exclamation: .word '!'
char_underscore: .word '_'

.text

_start:
    @p output_addr
    b!

    0x00 a!
    31 >r

init_buf_loop:
    '_' !+
    next init_buf_loop

    print_prompt

    0x00 a!
    write_prefix

    @p input_addr
    b!

    @b
    dup 10 xor if handle_empty_name
    dup if handle_null_char
    put_byte

    20 >r

read_name_loop:
    @b
    dup 10 xor if finish_name_read
    dup if handle_null_char
    put_byte
    next read_name_loop

    @b
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
    @b
    dup 10 xor if finish_name_read
    drop
    skip_rest_loop ;

finish_name_read:
    drop

    '!' put_byte
    0 put_byte

    @p output_addr
    b!

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
    stop

overflow:
    @p output_addr
    b!
    @p err_overflow
    !b
    stop

stop:
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
    @p char_W !b
    @p char_h !b
    @p char_a !b
    @p char_t !b
    @p char_space !b
    @p char_i !b
    @p char_s !b
    @p char_space !b
    @p char_y !b
    @p char_o !b
    @p char_u !b
    @p char_r !b
    @p char_space !b
    @p char_n !b
    @p char_a !b
    @p char_m !b
    @p char_e !b
    @p char_question !b
    10 !b
    ;

write_prefix:
    @p char_H put_byte
    @p char_e put_byte
    @p char_l put_byte
    @p char_l put_byte
    @p char_o put_byte
    @p char_comma put_byte
    @p char_space put_byte
    ;