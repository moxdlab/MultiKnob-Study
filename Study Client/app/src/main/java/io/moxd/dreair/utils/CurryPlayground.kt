package io.moxd.dreair.utils

/*
    for learning purposes only, do not waste time on reading this

 */

/*
    getting type errors? this function cannot be used generically and is made for the collision test
    only, to be able to provide a partially applied function so that the A2 argument comes last.
 */
fun <A1, A2, A3, A4, RES> ((A1, A2, A3, A4) -> RES).toCurry(): (A1) -> (A3) -> (A4) -> (A2) -> RES =
    { a1 ->
        { a3 ->
            { a4 ->
                { a2 ->
                    invoke(a1, a2, a3, a4)
                }
            }
        }
    }