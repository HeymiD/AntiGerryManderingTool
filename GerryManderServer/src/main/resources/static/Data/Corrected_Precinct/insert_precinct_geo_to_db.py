import pymysql
import json
connection = pymysql.connect(host='mysql4.cs.stonybrook.edu',
                             user='timberwolves',
                             password='changeit',
                             database='timberwolves',
                             charset='utf8mb4')
c = connection.cursor()
with open("Corrected_Presincts.geojsonl.json", 'r') as f:
    for line in f:
        precint_json = json.loads(line)
        pctkey = precint_json=precint_json["properties"]["PCTKEY"]
        p_geojson = json.dumps(line)
        query = "INSERT INTO Precincts_GEO (PCTKEY,geojson) VALUES (\"" + pctkey + "\", " + p_geojson + ")"
        c.execute(query)
        connection.commit()

