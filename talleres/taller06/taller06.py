
def cambioGreedy(cambio,denominaciones):
    denominaciones.sort()
    solucion = 0
    indice = len(denominaciones) - 1
    monedas = []
    while indice >= 0:
        if (denominaciones[indice] + solucion) <= cambio:
            solucion = solucion + denominaciones[indice]
            monedas.append(denominaciones[indice])
        else:
            indice = indice-1
    print(monedas)

denominaciones = [50, 10, 5,25, 1, 100]
cambioGreedy(40, denominaciones)




    