%ifndef COLON_INC
%define COLON_INC

%define dict_head 0

%macro colon 2
    %%entry:
        dq dict_head
        dq %%key
        dq %2
    %%key: db %1, 0
    %2:
    %define dict_head %%entry
%endmacro

%endif
