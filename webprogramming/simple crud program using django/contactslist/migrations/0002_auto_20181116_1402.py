# -*- coding: utf-8 -*-
# Generated by Django 1.11.16 on 2018-11-16 05:02
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('contactslist', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='post',
            name='e_mail',
            field=models.CharField(max_length=20),
        ),
        migrations.AlterField(
            model_name='post',
            name='name',
            field=models.CharField(max_length=20),
        ),
        migrations.AlterField(
            model_name='post',
            name='phone_number',
            field=models.CharField(max_length=20),
        ),
    ]
