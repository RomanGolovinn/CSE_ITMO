org 0x31B

start:  cla
        ld addr_str
        st ptr

char1:  in 0x07
        and m_rdy
        beq char1
        
        in 0x06
        and m_ff
        beq stop1
        
        st tmp
        ld c_8
        st cnt
        ld tmp
shift:  clc
        rol
        loop cnt
        jump shift
        st tmp

char2:  in 0x07
        and m_rdy
        beq char2
        
        in 0x06
        and m_ff
        beq stop2
        
        add tmp
        st (ptr)+
        jump char1

stop1:  st (ptr)
        hlt

stop2:  add tmp
        st (ptr)
        hlt

addr_str: word 0x05F8
ptr:      word 0x0000
tmp:      word 0x0000
cnt:      word 0x0000
c_8:      word 0x0008
m_rdy:    word 0x0040
m_ff:     word 0x00FF

; FC D4 C1 D6 28 00