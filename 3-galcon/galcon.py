import networkx as nx
import matplotlib.pyplot as plt
import random
import math
import time

def color(val):
    if val == 0: return [0.5, 0.5, 0.5]
    elif val>0: return [255/258, 195/258, 51/258]
    elif val<0: return [3/258, 37/258, 255/258] 

def make_moves(G, values, posMove, negMove):
    if (not (G.has_edge(negMove[0], negMove[1]) and G.has_edge(posMove[0], posMove[1]))):
        raise Exception ("Trying to move between two nodes that do not share an edge: " + str(negMove) + " " + str(posMove))
    if (values[negMove[0]] >= 0):
        raise Exception ("Negative team trying to move a node they don't control")
    if (values[posMove[0]] <= 0):
        raise Exception ("Pos team trying to move a node they don't control")
    #moves must be a two tuple (start, end)
    negAmt = math.floor(values[negMove[0]]/2)
    posAmt = math.ceil(values[posMove[0]]/2)
    values[negMove[0]] -= negAmt
    values[posMove[0]] -= posAmt
    #if each side attacks the same point
    if (negMove == posMove[::-1]):
        netAmt = posAmt + negAmt
        if netAmt == 0: pass
        elif netAmt < 0: values[negMove[1]] += netAmt
        else: values[posMove[1]] += netAmt
    else:
        values[negMove[1]] += negAmt
        values[posMove[1]] += posAmt
    return values
        
def add_values(values):
    return [(lambda x: x+1 if x>0 else x if x==0 else x-1)(v) for v in values]
def check_winner(values):
    if all(list(map(lambda x: x>=0, values))): return 1 
    elif all(list(map(lambda x: x<=0, values))): return -1
    else: return 0
def get_random_move(G, values, side):
    if side == -1: possibleStarts = [n[0] for n in filter(lambda v: v[1] < 0, enumerate(values))]
    elif side == 1: possibleStarts = [n[0] for n in filter(lambda v: v[1] > 0, enumerate(values))]
    else: raise Exception("Side must be 1 or -1")
    start = possibleStarts[random.randint(0, len(possibleStarts)-1)]
    possibleEnds = list(G.neighbors(start))
    end = possibleEnds[random.randint(0, len(possibleEnds)-1)]
    return [start, end]

def attack_weakest_neighbor(G, values, side):
    if side == -1: possibleStarts = [n[0] for n in filter(lambda v: v[1] < 0, enumerate(values))]
    elif side == 1: possibleStarts = [n[0] for n in filter(lambda v: v[1] > 0, enumerate(values))]
    else: raise Exception("Side must be 1 or -1")
    possibleEnds = set()
    for start in possibleStarts:
        for neighbor in list(G.neighbors(start)):
            possibleEnds.add(neighbor)
    if side == -1:
        #possible ends is the index
        possibleEnds = [end for end in possibleEnds if values[end] >= 0]
        end = min(possibleEnds, key=lambda x: values[x])
        possibleStarts = [start for start in list(G.neighbors(end)) if values[start] < 0]
        start = min(possibleStarts, key=lambda x: values[x])
    else:
        possibleEnds = [end for end in possibleEnds if values[end] <= 0]
        end = max(possibleEnds, key=lambda x: values[x])
        
        #end = values.index(max([values[end] for end in possibleEnds]))
        possibleStarts = [start for start in list(G.neighbors(end)) if values[start] > 0]
        start = max(possibleStarts, key=lambda x: values[x])
        print(values, possibleEnds, possibleStarts, end, start, list(G.neighbors(end)))
    return [start, end]



G = nx.gnp_random_graph(10, 0.5)
n = nx.number_of_nodes(G)

values = [0]*n

negBase = 0
posBase = n-1
values[negBase] = -1
values[posBase] = 1
for _ in range(50):
    labeldict = {}
    for i in range(n):
        labeldict[i] = values[i]
    plt.subplot(121)
    nx.draw(G, with_labels = True, pos = nx.circular_layout(G), labels = labeldict, node_color = list(map(color, values)))
    if check_winner(values) != 0: break
    plt.show(block=False)
    plt.pause(1)
    plt.close('all')
    values = add_values(values)
    negMove = attack_weakest_neighbor(G, values, -1)
    posMove = get_random_move(G, values, 1)
    values = make_moves(G, values, posMove, negMove)

print("Winner: " + str(check_winner(values)))
nx.draw(G, with_labels = True, pos = nx.circular_layout(G), labels = labeldict, node_color = list(map(color, values)))
plt.show()
