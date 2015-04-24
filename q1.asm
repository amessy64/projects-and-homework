#Alexander Messina
#
#Program inputs two numbers, uses then as indices to print an array
#               
        .text
	.globl main
main:
	li         $v0, 5          #reading a number  
	syscall
	addi       $s0, $v0, 0     #low is in $s0
	
	li         $v0, 5          #reading a number  
	syscall
	addi       $s1, $v0, 0     #high is in $s1
	
	subi       $sp, $sp, 8     #making space on the stack
	sw         $s0, 0($sp)     #push low to the stack
	sw         $s1, 4($sp)     #push high to the stack
	jal printarray
	
	sw         $s1, 0($sp)     #push low to the stack
	sw         $s0, 4($sp)     #push high to the stack
	jal printarray
	
	addi $sp, $sp, 8
	li $v0, 10		   #exit code = 10
	syscall
	
printarray:
	lw 	   $a0, 0($sp)    #startindex 
	lw	   $a1, 4($sp)	  #endindex
	la 	   $t4, LENGTH
	lw         $t4, ($t4)
	la    	   $t5, ARRAY    # t5 is the adress
	slt 	   $t0, $a0, $a1  #if start < end, t0 = 1 else t0 = -1
	bne  	   $t0, $zero, forward  #if not zero, then we're adding
	addi       $t0, $t0, -1        #if zero, then dir = -1
forward:
	add        $a1, $a1, $t0    #end = end + dir ---> want start to stop at end+-1
loop:
	bgt        $a0, $t4, done #if start > LENGTH(10), done
	blt 	   $a0, $zero, done #if start < 0, done
	beq        $a0, $a1, done #if start == end, done 
	
	mul   	   $t1, $a0, 4    #t1 will be the array offset
	add 	   $a0, $a0, $t0
	add        $t2, $t5, $t1  #t2 contains the address to be printed
	
	subi 	   $sp, $sp, 4   #need to protect a0
	sw         $a0, 0($sp)
	li         $v0, 1          #printing a number
	lw         $a0, ($t2)
	syscall
	lw 	   $a0, 0($sp)
	addi       $sp, $sp, 4   #done protecting a0
	
	beq        $zero, $zero, loop
	
done:
	jr $ra
	
        .data
ARRAY:  .word 10, 5, 2, 20, 20, -5, 3, 19, 9, 1
LENGTH: .word 10 
