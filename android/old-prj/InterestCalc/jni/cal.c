#include <stdio.h>
#include <math.h>

#define TAX_RATE_NORMAL 15.4f
#define TAX_RATE_GIVEN 9.5f

int deposit(int pay, int month, float interestRate, bool isSimple)
{
	/*[예금 이자 계산법_ 단리]
	단리 만기 일시지급 이자 = 원금 X (연이율/12) X 가입월수
	단리 월 이자 = 원금 X (연이율 X 개월수/12)	

	[예금 이자 계산법_ 단리]
	단리 만기 일시지급 이자 = 원금 X (연이율/12) X 가입월수
	단리 월 이자 = 원금 X (연이율 X 개월수/12)

	[예금 이자 계산법_ 복리]
	원리금 = 원금 X { 1 + (연이율/12) }  X 가입월수
	연간 이자원가 주기에 따른 원리금 = 원금 X { 1 + 연이율/(12/이자원가 주기) } X 가입월수 / 이자원가주기
	*/

	int res;
	int interest = pay * ((interestRate / 100) / 12 * month);

	if (isSimple)
	{
		int tax = interest * (TAX_RATE / 100);
		res = pay + month + interest - tax;
	}
	else
	{
		res = 
	}

	return res;
}

int installment(int pay, int month, float interestRate, bool isSimple)
{
	/*[정기적금 만기수령액 계산법]
	원금 = 월 납입액 X 계약 월 수 
	이자 = 월 납입액 X 연이율/12 X [계약 월 X 수(계약 월수 + 1)/2] 
	세금 = 이자 X 15.4% 

	실수령액 = 원금 + 이자 - 세금*/

	int res;
	if (isSimple)
	{
		res = (pay * month) + 
	}
	else
	{

	}
}

int interest(int pay, int month, float interestRate, bool isSimple)
{
	int res;

	if (isSimple)
	{
		res = pay *
	}
	else
	{

	}
}