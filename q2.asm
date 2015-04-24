.text
.globl main

main:
	subi $sp, $sp, 4
	la $t5, prompt
	sw $t5, 0($sp)
	jal puts
	addi $sp, $sp, 4

	subi $sp, $sp, 8	#make room on the stack
	la $t0, buffer
	sw $t0, 0($sp)		#t0 is the ptr
	lw $t1, size
	sw $t1, 4($sp)		#t1 is limit
	jal gets		#call gets
	
	subi $sp, $sp, 4
	la $t5, entered
	sw $t5, 0($sp)
	jal puts
	addi $sp, $sp, 4
	
	subi $sp, $sp, 4
	la $t5, buffer
	sw $t5, 0($sp)
	jal puts
	addi $sp, $sp, 4
	
	li $v0, 10		   #exit code = 10
	syscall

#getChar
getchar:
	lui $a3, 0xffff		#base adress of memory map
ckready:
	lw $t1, 0($a3)		#read from receiver control register
	andi $t1, $t1, 0x0001	#extract ready bit
	beqz $t1, ckready	#if 0, not ready and loop -> polling method
	lw $v0, 4($a3)		#loading character from keyboard
	jr $ra			#return to caller
	
#gets(ptr, limit)
gets:
	lw $a0, 0($sp)		#ptr to string being written to
	lw $a1, 4($sp)		#limit to number of characters being read

	subi $sp, $sp, 8	#make space for variables if needed
	sw $s0, 0($sp)
	sw $ra, 4($sp)
	move $s0, $a0		#now we use s0 for the pointer, the caller has a0 unchanged

test1:
	beqz $a1, steg		#leave if limit == 0
	subi $a1, $a1, 1	#limit--
	jal getchar		#do getchar driver, polling
	sb $v0, ($s0)
	addi $s0, $s0, 1	#move ptr down 1 byte for next char
	beq $v0, 10, steg	#10 = new line
	b test1
	
steg:
	move $v0, $a0
	lw $s0, 0($sp)
	lw $ra, 4($sp)
	addi $sp, $sp, 4
	jr $ra

#putChar
putchar:
	lui $a3, 0xffff		#base adress of memory map
xready:
	lw $t1, 8($a3)		#read from transmitter control register
	andi $t1, $t1, 0x1	#extract ready bit
	beqz $t1, xready	#if 0, not ready and loop -> polling method
	sw $a0, 12($a3)		#writing character
	jr $ra			#return to caller
	
#puts(ptr)	
puts:
	lw $a1, 0($sp)		#ptr to string being written to

	subi $sp, $sp, 8	#make space for variables if needed
	sw $s0, 0($sp)
	sw $ra, 4($sp)

test2:
	lb $a0, ($a1)
	addi $a1, $a1, 1
	beq $a0, 0, stup
	jal putchar		#do putchar driver, polling
	b test2
	
stup:
	move $v0, $a0
	lw $s0, 0($sp)
	lw $ra, 4($sp)
	addi $sp, $sp, 4
	jr $ra
	
.data
prompt: .asciiz "Enter your first and last name: "
entered:.ascii "You entered: "
comma: .ascii ", "
end: .asciiz ""
buffer: .space 100
size: .word 40