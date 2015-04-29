import uuid
from cqlengine import columns
from cqlengine.models import Model

class Tweets(Model):
    read_repair_chance = 0.05  # optional - defaults to 0.1
    id = columns.Integer(primary_key=True)
    content = columns.Text()
    username = columns.Text()
    lat = columns.Float()
    lng = columns.Float()

class TopHashtags(Model):
    read_repair_chance = 0.05  # optional - defaults to 0.1
    hashtag = columns.Text(primary_key=True)
    count = columns.BigInt()
