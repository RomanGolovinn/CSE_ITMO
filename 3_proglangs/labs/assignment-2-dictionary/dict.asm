global find_word
%include "lib.inc"

section .text

find_word:
    push r12
    push r13
    sub rsp, 8

    mov r12, rdi                
    mov r13, rsi                

.loop:
    test r13, r13
    jz .not_found

    mov rdi, r12
    mov rsi, [r13 + 8]          
    call string_equals
    test rax, rax
    jnz .found

    mov r13, [r13]              
    jmp .loop

.found:
    mov rax, r13
    jmp .done

.not_found:
    xor eax, eax

.done:
    add rsp, 8
    pop r13
    pop r12
    ret
