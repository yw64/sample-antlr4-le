grammar LogicExpr;

// ʾ����(a) or (b) and (c)

stat: expr ;

expr: expr AND expr				# and
    | expr OR expr				# or
    | '(' expr ')'				# group
    | VAR							# var
    ;

VAR : '(' KEY ')' ;

AND: 'and' ;
OR: 'or' ;
KEY: [a-z]+ ;
WS: [ \t]+ -> skip ;
