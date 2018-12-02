from django.db import models

class Post(models.Model):
    name = models.CharField(max_length=20)
    e_mail = models.EmailField(max_length=20)
    phone_number = models.CharField(max_length=20)

    def publish(self):
        self.save()

    def __str__(self):
        return self.name