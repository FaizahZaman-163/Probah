<program> ::= <statement-list>

<statement-list> ::= <statement> <statement-list>
                   | ε


<statement> ::= <declaration> ";"
              | <assignment> ";"
              | <print-statement> ";"
              | <if-statement>
              | <while-statement>


<declaration> ::= "ধরি" <type> <identifier> ":=" <expression>

<type> ::= "পূর্ণ"
         | "যুক্তি"


<assignment> ::= <identifier> ":=" <expression>


<print-statement> ::= "দেখাও" <expression>


<if-statement> ::=
      "যদি" "(" <expression> ")" <block> <else-option>


<else-option> ::=
      "নাহলে" <block>
    | "নাহলে" <if-statement>
    | ε



<block> ::= "{" <statement-list> "}"


<expression> ::= <or-expression>


<or-expression> ::=
      <and-expression>
      { "অথবা" <and-expression> }


<and-expression> ::=
      <equality-expression>
      { "এবং" <equality-expression> }


<equality-expression> ::=
      <comparison-expression>
      { ("==" | "!=") <comparison-expression> }


<comparison-expression> ::=
      <addition-expression>
      [ ("<" | "<=" | ">" | ">=")
        <addition-expression> ]


<addition-expression> ::=
      <multiplication-expression>
      { ("+" | "-") <multiplication-expression> }


<multiplication-expression> ::=
      <unary-expression>
      { ("*" | "/") <unary-expression> }


<unary-expression> ::=
      ("না" | "-") <unary-expression>
    | <primary-expression>


<primary-expression> ::=
      <integer-literal>
    | "সত্য"
    | "মিথ্যা"
    | <identifier>
    | "(" <expression> ")"