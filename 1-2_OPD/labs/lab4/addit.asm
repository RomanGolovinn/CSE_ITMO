org 0x100
word 0x8001, 0x8001, 0x8fff
res: word 0x000
arrpointer: word 0x100
counter: word 0x3

start: cla
iter: ld (arrpointer)+
call func
add res
st res
loop counter
jump iter
ld res
call func
st res
hlt


func: 
and m_0fff
st tmp
and m_0800
beq pos
ld tmp
add m_0fff
ret
pos: ld tmp
ret

m_0fff: word 0x0fff
m_0800: word 0x0800
m_f000: word 0x0f00
tmp: word 0x000
