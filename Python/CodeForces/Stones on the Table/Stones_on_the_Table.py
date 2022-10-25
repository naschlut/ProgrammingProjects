def Stones_on_the_Table(str):
    total = 0
    order = list(str)
    for i in range(len(order)-1):
        if order[i]==order[i+1]:
            total +=1
    return total



if __name__ == '__main__':
    times = int(input())
    order = input()
    print(Stones_on_the_Table(order))