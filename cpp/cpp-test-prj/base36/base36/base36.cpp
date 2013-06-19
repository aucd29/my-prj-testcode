// base36.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "Base36.h"


int _tmain(int argc, _TCHAR* argv[])
{
	std::string res;
	CBase36 base36;

	res = base36.encode(100);
	printf("%s\n", res.c_str());

	res = base36.encode(1000);
	printf("%s\n", res.c_str());

	res = base36.encode(10000);
	printf("%s\n", res.c_str());

	res = base36.encode(100000);
	printf("%s\n", res.c_str());

	int intValue = base36.decode(res);
	printf("%d\n", intValue);


	return 0;
}

