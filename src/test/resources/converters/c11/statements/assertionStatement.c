//void P(int in) {
//    if (x == 1) {
//        if (y == 2) {
//            if (z == 3) {
//                a = 4;
//            }
//        }
//        else {
//            b = 5;
//        }
//    }
//    else {}
//}

void X() {
    if (1) {
        a = 2;
        while (3) {
            if (101) ;
            while (0) ;
            if (4) {
                break;
            }
            else {
                continue;
            }
            while (4) {
                b = 3;
                if (6) break;
            }
            c = 4;
        }
    }

    if (100) {}
    x;
}

//void P9 (int a) {
//    if (cond1) {
//        a = 1;
//        a = 3;
//        if (cond2) {
//            if (cond3) {
//                a = 1;
//            }
//        }
//        else if (x == 2) {
//            a = 5;
//        }
//        else {
//            a = 6;
//        }
//    }
//} //nested declarator
//
//void P() {
////    while (cond1) {
////        expr;
//        if (cond2) {
//            if (x) {
//                x = 2;
//            }
//            if (cond3) {
//                //break;
//            }
//            if (cond4) {
//                a = 4;
//                //continue;
//            }
//            if (cond5) {
//                a = 2;
//                //continue;
//                //dead_code;
//            }
//            else {
//                code_false;
//            }
//        }
//
//        if (cond5) {//again cond5
//            break;
//        }
//        a = 1;
////    }
//}

//void P1(spinlock_t *sl, long long *x0, int& x1, char c) {
//    y = x > 100500;
//    while (x == 1) ;
//
//    while (0) {
//        x = 1.1;
//        while (1.5) ;
//        if (x == 2) {
//            while (x == 3) {
//                a = 4;
//            }
//            continue;
//        }
//        else {
//            b = 5;
//            break;
//        }
//    }
//}


//    lll int r0;
//    void* a, b, c ,d, e;
//	if (sl == 1)
//	    if (sl2 == 2)
//	        {
//	            return;
//	        }
//    {
//        a[2].print(*x1, x1->x3);
//        a[i] = 3;
//    }
//	lbl int r1;
//	while ( (a == 3 && b == 3) || x) {
//        sp( &a);
//    }
//    do {
//    	function_1_1(sl);
//    } while (true);
//
//	if (x) goto lbl;
//
//	function_1_2(*x0, 1);
//	r1 = function_1_3(*x1);
//	function_1_4(sl);

/*P2 {
	int r2 ;//= function_2_1(sl);
	function_2_2(*x0, 1);
}

int** P3(type_3 *sl, int *x0, int *x1)
{
	int r3;
	function_3_1(sl);
}
*/

/*
//exists(y == 22.3 \/ ( 0 :y == 1 /\ 2:x == 3:y /\ 4:x == 5:y \/ 100:a==0 )  );
//exists (r1=0 /\ 1:r1=0)

/*


P0(spinlock_t *sl, int *x0, int *x1)
{
	int r1;

	spin_lock(sl);
	WRITE_ONCE(*x0, 1);
	r1 = READ_ONCE(*x1);
	spin_unlock(sl);
}

P1(spinlock_t *sl, int *x0, int *x1)
{
	int r1;

	spin_lock(sl);
	WRITE_ONCE(*x1, 1);
	r1 = READ_ONCE(*x0);
	spin_unlock(sl);
}
*/

/*
void P0() {
    c1 = 1;
    c1 = 1L;
    c1 = 0b1;
    c2 = 0x99;
    c2 = 0xF;
    c2 = 03;
    a3 = 11;
    b = 1.2;
}

void P1 (int a, float& v) {
    if (a == 23) { v  = 3; }
}

P1 (int a, float& v) {
    if (a == 23) { v  = 3; }
}

struct s {
    int   x;
    float y;
    char  *z;
} tee;
//And the following statement will declare a similar union named u and an instance of it named n:
union u {
    int   x;
    float y;
    char  *z;
} n;


exists(y == 1 /\ (1:x == 2) /\ 2:x == 3);
//exists(y == 22.3 \/ (0:y == 1 /\ 1:z==0 ) \/ (2:a == 2 || 3:b == 3) && c == 4);
//exists(y == 22.3 \/ (0:y == 1 /\ 2:x == 3:y /\ 4:x == 5:y \/ 100:a==0 )  );
*/