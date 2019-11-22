with open('texas_districts.geojsonl.json', 'r') as f:
    i=1
    for line in f:
        with open('District_' + str(i) + '.geojson', 'w') as newfile:
            newfile.write(line)
        i=i+1




