org 0x100
word 0x8fff, 0x8001, 0x8ffa
res: word 0x000
arrpointer: word 0x100
counter: word 0x3

start:  cla
iter:   ld (arrpointer)+
        push
        call func
        pop
        add res
        st res
        loop counter
        jump iter
        ld res
        and m_0fff
        st res
        hlt

func:   ld &1
        and m_0fff
        st tmp
        and m_0800
        beq is_pos
        ld tmp
        add m_f000
        jump save
is_pos: ld tmp
save:   st &1
        ret

m_0fff: word 0x0FFF
m_0800: word 0x0800
m_f000: word 0xF000
tmp:    word 0x0000