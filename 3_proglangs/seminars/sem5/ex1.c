#include "mac.h"
#include <stdint.h>

#define ANSWER 42

int main(void) {
    int x = 42;

    print_var(x);
    print_var(42);
    print_var(ANSWER);  

    return 0;
}