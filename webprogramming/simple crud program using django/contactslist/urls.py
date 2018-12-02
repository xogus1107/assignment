from django.conf.urls import url
from . import views

app_name='contactslist'

urlpatterns = [
    url(r'^$', views.post_list, name='post_list'),
    url(r'^create$', views.create),
    url(r'^edit/(?P<id>\d+)$', views.edit),
    url(r'^update/(?P<id>\d+)$', views.update),
    url(r'^delete/(?P<id>\d+)$', views.destroy),
    url(r'^search/$', views.search),
    url('search/', views.search, name='search'),
    url('search/<str:name>/', views.search_specific),
]