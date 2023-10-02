package works.hop.first

static void main(String[] args) {
    println "Hello world!"

    //handling lists
    def alphaList = ['a', 'b', 'c']
    alphaList[4] = 'd'
    alphaList << 'e'

    assert alphaList == ['a', 'b', 'c', null, 'd', 'e']
    println alphaList

    //handling maps
    def alphaMap = [a: 'ant', 'b': 'bird', c: 'cat']
    alphaMap['b'] = 'baboon'
    alphaMap.d = 'dog'
    alphaMap['e'] = 'elk'
    alphaMap << [f: 'flamingo']

    assert alphaMap == [a: 'ant', b: 'baboon', c: 'cat', d: 'dog', e: 'elk', f: 'flamingo']
    println alphaMap

    //iterators
    def numbers = [1, 2, 3, 4, 5]
    numbers.each { println it * 2 }
    numbers.each { num -> println num * 3 }
    println numbers.find { it % 2 == 0 }
    println numbers.findAll { it % 2 == 0 }
    println numbers.collect { it * 2 } //similar to map function in javascript
    println numbers.any { it % 2 == 0 }
    println numbers.every { it % 2 == 0 }
    println numbers.findIndexOf { it % 2 == 0 }
    println numbers.sum()
    println numbers.min()
    println numbers.max()
    println numbers.average()
}

show = { println it }
sqrt = { Math.sqrt(it) }

static def please(action) {
    [the: {
        what ->
            [of: {
                number -> action(what(number))
            }]
    }]
}

please show the sqrt of 100

def sayHello = { name ->
    println "Hello, ${name}!"
}

sayHello("Jim")

