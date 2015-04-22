import numpy as np
import matplotlib.pyplot as plt
dictionary = {}
myFile = open("Graphs.txt", "r")
myList =[]

for x in myFile:
	#split the line from the file into the number and the hashtag
	items = x.split(" ")
	myList.append(int(items[0]))	
	myList.sort(reverse=True)
	for z in items:
		#make the key the number, and the answer the hashtag
			
		key = items[0]		
		answer = items[1]
		dictionary[key] = answer


n_groups = 5

hashtags = (int(myList[0]), int(myList[1]), int(myList[2]), int(myList[3]), int(myList[4]))

index = np.arange(n_groups)
bar_width = .5
opacity = 0.75
error_config = {'ecolor': '0'}

plt.bar(index, hashtags, bar_width,
                 alpha=opacity,
                 color='b',
                 yerr=1,
                 error_kw=error_config,
                 label='Hashtags')


print(myList)
print(dictionary)
plt.xlabel('Hashtags')
plt.ylabel('Number of times used')
plt.title('Most Common Hashtags')
plt.xticks(index + bar_width, (dictionary[str(myList[0])], dictionary[str(myList[1])], dictionary[str(myList[2])], dictionary[str(myList[3])], dictionary[str(myList[4])]))
plt.show()

