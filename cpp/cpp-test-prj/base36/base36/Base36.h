#pragma once

#include <string>

const std::string baseKey = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

class CBase36
{
public:
	CBase36(void)
		: _base(36)
	{

	}

	~CBase36(void)
	{
	}

public:
	std::string encode(int num)
	{
		int remainder;
		std::string temp, result;

		do 
		{
			remainder = num % _base;
			temp = baseKey.substr(remainder, 1);
			result = temp + result;

			num  = (num - remainder) / _base;

		} while(num > 0);

		return result;
	}

	long decode(std::string base32)
	{
		if (base32.size() == 0)
		{
			return false;
		}

		long res = 0;
		std::string temp;

		do 
		{
			temp  = base32.substr(0, 1);
			base32 = base32.substr(1, base32.size() - 1);

			size_t pos = baseKey.find(temp);
			if (pos == std::string::npos)
			{
				return false;
			}

			res = (res * _base) + pos;
		} while(base32.size() > 0);

		return res;
	}

private:
	int _base;
};

