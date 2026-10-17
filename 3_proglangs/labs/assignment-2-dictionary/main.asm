%include "lib.inc"
%include "dict.inc"

section .bss
input_buffer: resb 256

section .rodata
input_error: db "Input is empty or the word is too long.", 10
input_error_end:
not_found_error: db "Word not found.", 10
not_found_error_end:

section .data
%include "words.inc"

section .text
global _start

_start:
    xor r12d, r12d

.read_line:
    call read_char
    test rax, rax
    jz .line_complete

    cmp al, 10
    je .line_complete
    cmp al, 13
    je .line_complete

    cmp r12, 255
    jae .input_error
    mov [input_buffer + r12], al
    inc r12
    jmp .read_line

.line_complete:
    mov byte [input_buffer + r12], 0
    test r12, r12
    jz .input_error

    mov rdi, input_buffer
    mov rsi, dict_head
    call find_word
    test rax, rax
    jz .not_found

    mov rdi, [rax + 16]
    call print_string
    call print_newline

    xor edi, edi
    call exit

.input_error:
    mov rsi, input_error
    mov edx, input_error_end - input_error
    jmp .write_error

.not_found:
    mov rsi, not_found_error
    mov edx, not_found_error_end - not_found_error

.write_error:
    mov eax, 1
    mov edi, 2
    syscall

    mov edi, 1
    call exit
