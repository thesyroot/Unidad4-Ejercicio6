# Casos de Prueba - Sincronizacion de Precios de Insumos

## Escenario 1: Sincronizacion exitosa con cambio de dolar

**Dado** que existen insumos activos en la base de datos con `valorDolarReferencia = 1400`
**Y** la API de dolar oficial retorna `venta = 1425`
**Cuando** el cronjob `syncPrices()` se ejecuta
**Entonces** el sistema debe actualizar `valorDolarReferencia` a `1425` para cada insumo
**Y** debe recalcular `precioEnPesos = precioEnDolares * 1425`
**Y** debe persistir los cambios en la base de datos

## Escenario 2: Sin actualizacion cuando el dolar no cambio

**Dado** que existen insumos activos con `valorDolarReferencia = 1425`
**Y** la API de dolar oficial retorna `venta = 1425`
**Cuando** el cronjob `syncPrices()` se ejecuta
**Entonces** el sistema no debe modificar ningun insumo
**Y** no debe invocar `save()` en el repositorio

## Escenario 3: Lista vacia de insumos

**Dado** que no existen insumos activos en la base de datos
**Y** la API de dolar oficial retorna `venta = 1425`
**Cuando** el cronjob `syncPrices()` se ejecuta
**Entonces** el sistema no debe invocar `save()` en el repositorio

## Escenario 4: Error en la API de dolar

**Dado** que existen insumos activos en la base de datos
**Y** la API de dolar oficial falla con un error de conexion
**Cuando** el cronjob `syncPrices()` se ejecuta
**Entonces** el sistema debe capturar la excepcion y registrar el error en el log
**Y** no debe modificar ningun insumo en la base de datos

## Escenario 5: Solo insumos con valor diferente se actualizan

**Dado** que existen insumos activos:
- Insumo A con `valorDolarReferencia = 1400`
- Insumo B con `valorDolarReferencia = 1425`
**Y** la API de dolar oficial retorna `venta = 1425`
**Cuando** el cronjob `syncPrices()` se ejecuta
**Entonces** solo el Insumo A debe ser actualizado
**Y** el Insumo B debe permanecer sin cambios

## Escenario 6: Consumo exitoso de API DolarApi

**Dado** una solicitud a `https://dolarapi.com/v1/dolares/oficial`
**Cuando** el servicio `DolarApiService.getDolarOficialVenta()` es invocado
**Entonces** debe retornar un `Double` con el valor de venta del dolar oficial
**Y** no debe lanzar ninguna excepcion

## Escenario 7: Error HTTP al consumir API

**Dado** que el servicio `DolarApiService` realiza una peticion HTTP
**Y** el servidor responde con codigo de estado `500`
**Cuando** se invoca `getDolarOficialVenta()`
**Entonces** debe lanzar una `RuntimeException`
**Y** el mensaje debe indicar un error HTTP

## Escenario 8: Timeout en llamada a API

**Dado** que el servicio `DolarApiService` realiza una peticion HTTP
**Y** el cliente HTTP lanza una excepcion de timeout
**Cuando** se invoca `getDolarOficialVenta()`
**Entonces** debe lanzar una `RuntimeException`
**Y** el mensaje debe contener "Connection timeout"

## Escenario 9: Ejecucion programada del cronjob

**Dado** la configuracion `@Scheduled(cron = "0 0 * * * 1-5")`
**Cuando** sea las `HH:00` de un dia lunes a viernes
**Entonces** el metodo `syncPrices()` debe ejecutarse automaticamente
**Y** no debe ejecutarse los sabados ni domingos

## Escenario 10: Integracion completa

**Dado** que existen 3 insumos activos en la base de datos
**Y** la API de dolar oficial retorna un valor valido
**Cuando** el cronjob se ejecuta
**Entonces** todos los insumos con valor desactualizado deben actualizarse
**Y** los precios en pesos deben recalcularse correctamente
**Y** el sistema debe continuar funcionando sin errores
