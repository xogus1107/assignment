
programs = package_delivery

all: $(programs)

package_delivery: package_delivery.c
	gcc $^ -o $@

#gcc -g $^ -o $@

clean:
	rm -f $(programs)

