import sys
import json

def get_image(path):
    with open(path) as j_file:
        cat_json = json.load(j_file)

    values_dots = []
    for val in cat_json:
        bluetooth_data = val['_source']['layers']['btatt']
        if bluetooth_data.get('btatt.value'):
            values_dots.append(bluetooth_data['btatt.value'])

    values_str = [x.replace(':', '') for x in values_dots]
    return values_str

if __name__ == '__main__':
    try:
        json_path = sys.argv[1]
    except Exception:
        print('Usage: {}.py file.json'.format(sys.argv[0]))
        exit(1)
    print(get_image(json_path))

# Start:

# '51:78:a3:00:01:00:00:00:ff:51:78:a4:00:01:00:33:99:ff:51:78:a6:00:0b:00:aa:55:17:38:44:5f:5f:5f:44:38:2c:a1:ff:51:78:af:00:02:00:e0:2e:89:ff:51:78:be:00:01:00'

# End:

# '02:00:30:00:f9:ff:51:78:a1:00:02:00:30:00:f9:ff:51:78:bd:00:01:00:19:4f:ff:51:78:a6:00:0b:00:aa:55:17:00:00:00:00:00:00:00:17:11:ff:51:78:a3:00:01:00:00:00:ff'
