from django.shortcuts import render, redirect
from .models import Post
from django.template import loader
from django.db.models import Q
from django.contrib import messages
from django.http import HttpResponse, HttpResponseRedirect
# Create your views here.
def post_list(request):
    person = Post.objects.all()
    context={'person': person}
    return render(request, 'blog/post_list.html', context)

def create(request):
    print(request.POST)
    person = Post(name=request.POST['name'], e_mail=request.POST['e_mail'],
                  phone_number=request.POST['phone_number'])

    person.save()
    return redirect('/')

def edit(request, id):
    person = Post.objects.get(id=id)
    context = {'person': person}
    return render(request, 'blog/edit.html',context)

def update(request, id):
    person = Post.objects.get(id=id)
    person.name = request.POST['name']
    person.e_mail = request.POST['e_mail']
    person.phone_number = request.POST['phone_number']
    person.save()
    return redirect('/')

def destroy(request, id):
    people = Post.objects.get(id=id)
    people.delete()
    return redirect('/')

def search(request):
    searchName = request.GET.get('name','')
    if searchName != "":
        return search_specific(request, searchName)
    template = loader.get_template('blog/search.html')
    contacts = Post.objects.order_by('pk')
    context = {
        'contacts' : contacts,
    }
    return HttpResponse(template.render(context, request))

def search_specific(request, name):
    searched = Post.objects.filter(name__icontains=name)
    context = {
        'name' : name,
        'contacts' : searched,
    }
    template = loader.get_template('blog/searchedName.html')
    return HttpResponse(template.render(context, request))