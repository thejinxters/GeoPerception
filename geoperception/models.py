import uuid
from cqlengine import columns
from cqlengine.models import Model

class Tweets(Model):
    read_repair_chance = 0.05  # optional - defaults to 0.1
    id = columns.Integer(primary_key=True)
    content = columns.Text()
    username = columns.Text()
