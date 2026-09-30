section .text

global _start
_start:
    call    f
    mov     rax, 60          ; 'exit' syscall number
    xor     rdi, rdi         ; error code
    syscall

f:
    call    g
    ret

g: 
    ret
