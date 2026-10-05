section .text
 
global exit
global string_length
global print_string
global print_char
global print_newline
global print_uint
global print_int
global string_equals
global read_char
global read_word
global parse_uint
global parse_int
global string_copy


; Принимает код возврата и завершает текущий процесс
exit:
    mov rax, 60
    syscall

; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length:
    xor rax, rax
.loop:
    cmp byte [rdi + rax], 0
    je .done
    inc rax
    jmp .loop
.done:
    ret

; Принимает указатель на нуль-терминированную строку, выводит её в stdout
print_string:
    push rdi
    call string_length
    mov rdx, rax
    pop rsi
    mov rax, 1
    mov rdi, 1
    syscall
    ret

; Принимает код символа и выводит его в stdout
print_char:
    push rdi

    mov rax, 1
    mov rdi, 1
    mov rsi, rsp
    mov rdx, 1
    syscall

    pop rdi
    ret

; Переводит строку (выводит символ с кодом 0xA)
print_newline:
    mov rdi, 0xA
    jump print_char


; Выводит беззнаковое 8-байтовое число в десятичном формате 
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
print_uint:
    ; 40 байт: 32 под цифры и 8 для выравнивания стека перед вызовами
    sub rsp, 40

    mov rax, rdi
    mov r8, rsp
    add r8, 31
    xor r10, r10

    test rax, rax
    jnz .convert

    mov byte [r8], '0'
    mov r10, 1
    jmp .write
.convert:
    xor rdx, rdx
    mov r9, 10
    div r9

    add dl, '0'
    mov byte [r8], dl

    dec r8
    inc r10

    test rax, rax
    jnz .convert

.print:
    inc r8

.write:
    mov rax, 1
    mov rdi, 1
    mov rsi, r8
    mov rdx, r10
    syscall

    add rsp, 40
    ret


; Выводит знаковое 8-байтовое число в десятичном формате 
print_int:
    push rbx
    mov rbx, rdi
    test rdi, rdi
    jns .positive

    mov rdi, '-'
    call print_char
    mov rdi, rbx
    neg rdi

.positive:
    call print_uint
    pop rbx
    ret


; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals:
.loop:
    mov al, [rdi]
    cmp al, [rsi]
    jne .not_equal

    test al, al
    jz .equal

    inc rdi
    inc rsi
    jmp .loop

.equal:
    mov rax, 1
    ret

.not_equal:
    xor rax, rax
    ret


; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char:
    sub rsp, 8

    xor eax, eax        ; syscall read = 0
    xor edi, edi        ; stdin = 0
    mov rsi, rsp        ; buffer
    mov edx, 1          ; 1 byte
    syscall

    cmp eax, 1
    jne .eof

    movzx eax, byte [rsp]

    add rsp, 8
    ret

.eof:
    xor eax, eax
    add rsp, 8
    ret 

; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор

read_word:
    push r12
    push r13
    push r14
    push r15
    sub rsp, 8

    mov r12, rdi        ; buffer
    mov r13, rsi        ; buffer size
    xor r14, r14        ; length

.skip_spaces:
    call read_char
    test rax, rax
    jz .fail

    cmp al, ' '
    je .skip_spaces
    cmp al, 9
    je .skip_spaces
    cmp al, 10
    je .skip_spaces

    ; Первый символ слова
    ; Оставляем один байт в буфере для нуль-терминатора.
    cmp r13, 1
    jbe .fail
    mov r15, r13
    dec r15
    cmp r14, r15
    jae .fail

    mov [r12 + r14], al
    inc r14

.read_loop:
    call read_char
    test rax, rax
    jz .finish

    cmp al, ' '
    je .finish
    cmp al, 9
    je .finish
    cmp al, 10
    je .finish

    ; Нужен ещё один байт + \0
    mov r15, r13
    dec r15
    cmp r14, r15
    jae .fail

    mov [r12 + r14], al
    inc r14
    jmp .read_loop

.finish:
    mov byte [r12 + r14], 0

    mov rax, r12
    mov rdx, r14

    add rsp, 8
    pop r15
    pop r14
    pop r13
    pop r12
    ret

.fail:
    xor eax, eax
    xor edx, edx

    add rsp, 8
    pop r15
    pop r14
    pop r13
    pop r12
    ret
 

; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
parse_uint:
    xor rax, rax        ; result = 0
    xor rdx, rdx        ; length = 0

.loop:
    movzx rcx, byte [rdi]

    cmp cl, '0'
    jb .done

    cmp cl, '9'
    ja .done

    sub cl, '0'
    imul rax, rax, 10
    add rax, rcx

    inc rdx
    inc rdi

    jmp .loop

.done:
    ret



; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось
parse_int:
    sub rsp, 8
    xor rdx, rdx

    movzx rcx, byte [rdi]

    cmp cl, '-'
    je .negative

    cmp cl, '+'
    je .positive

    jmp .parse

.negative:
    inc rdi
    call parse_uint

    test rdx, rdx
    jz .fail

    neg rax
    inc rdx
    add rsp, 8
    ret

.positive:
    inc rdi
    call parse_uint

    test rdx, rdx
    jz .fail

    inc rdx
    add rsp, 8
    ret

.parse:
    call parse_uint
    add rsp, 8
    ret

.fail:
    xor eax, eax
    xor edx, edx
    add rsp, 8
    ret

; Принимает указатель на строку, указатель на буфер и длину буфера
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0
string_copy:
    call string_length
    mov r8, rax              

    inc r8
    cmp r8, rdx
    ja .fail

    xor rcx, rcx

.copy:
    mov al, [rdi + rcx]
    mov [rsi + rcx], al

    cmp al, 0
    je .success

    inc rcx
    jmp .copy

.success:
    mov rax, r8
    dec rax
    ret

.fail:
    xor eax, eax
    ret