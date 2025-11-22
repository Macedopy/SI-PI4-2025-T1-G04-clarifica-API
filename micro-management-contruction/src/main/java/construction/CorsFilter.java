// package construction;

// import jakarta.ws.rs.container.ContainerRequestContext;
// import jakarta.ws.rs.container.ContainerResponseContext;
// import jakarta.ws.rs.container.ContainerResponseFilter;
// import jakarta.ws.rs.ext.Provider;
// import java.io.IOException;

// @Provider // ESSENCIAL para o Quarkus reconhecer
// public class CorsFilter implements ContainerResponseFilter {

//     @Override
//     public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
//         responseContext.getHeaders().add("Access-Control-Allow-Origin", "*"); 
//         responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
//         responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");

//         if (requestContext.getMethod().equals("OPTIONS")) {
//             responseContext.getHeaders().add("Access-Control-Max-Age", "1728000"); 
//         }
//     }
// }