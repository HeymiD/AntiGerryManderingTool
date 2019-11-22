import csv

with open('C:\\Users\mikew\CSE308\DummyData.csv', newline='') as csvfile:
    spamreader = csv.DictReader(csvfile)
    newFilePath = 'C:\\Users\mikew\CSE308\DummyDataNew.csv'
    writeFile = open('C:\\Users\mikew\CSE308\DummyDataNew.csv')
    fieldNames = ['County','cntyvtd','Office','Democrat','Republican','Green','Libertarian']
    writer = csv.DictWriter(writeFile,fieldnames = fieldNames)
    dictionary = {}
    set = []
    for row in spamreader:
        dictionary[row['cntyvtd']] = {'County':row['ï»¿County'], 'cntyvtd':row['cntyvtd'], 'Office':row['Office'],
                                      'Democrat':row['Democrat'],'Republican':row['Republican'],'Green':row['Green'],
                                      'Libertarian':row['Libertarian']}
        set.append(row['cntyvtd'])
    for i in range(len(set)):
        substr = set[i][:len(set[i])-1]
        if(substr not in dictionary.keys()):
            dictionary[substr] = {'County':dictionary[set[i]]['County'], 'cntyvtd':substr,
                                'Office':dictionary[set[i]]['Office'],
                                    'Democrat':dictionary[set[i]]['Democrat'],
                                    'Republican':dictionary[set[i]]['Republican'],'Green':dictionary[set[i]]['Green'],
                                    'Libertarian':dictionary[set[i]]['Libertarian']}
        else:
            dictionary[substr]['Democrat'] = int(dictionary[substr]['Democrat']) + int(dictionary[set[i]]['Democrat'])
            dictionary[substr]['Republican'] = int(dictionary[substr]['Republican']) + int(dictionary[set[i]]['Republican'])
            dictionary[substr]['Green'] = int(dictionary[substr]['Green']) + int(dictionary[set[i]]['Green'])
            dictionary[substr]['Libertarian'] = int(dictionary[substr]['Libertarian']) + int(dictionary[set[i]]['Libertarian'])
        del dictionary[set[i]]
    for i in dictionary:
        print(dictionary[i].values())



