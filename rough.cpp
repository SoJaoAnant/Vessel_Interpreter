#include<bits/stdc++.h>
using namespace std;
 
bool is_odd(int n);
bool is_even(int n);

int main()
{
    int num = 5012;

    if(is_even(num))
    {
        cout << "number is even" << endl;
    }
    
    if(is_odd(num))
    {
        cout << "number is odd" << endl;
    }
 
    return 0;
}

bool is_odd(int n)
{
    if(n == 0)
    {
        return false;
    }
    return is_even(n-1);
} 

bool is_even(int n)
{
    if(n == 0)
    {
        return true;
    }
    return is_odd(n-1);
} 