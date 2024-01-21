/**
 * This file was auto-generated by openapi-typescript.
 * Do not make direct changes to the file.
 */


export interface paths {
  "/api/v1/posts": {
    get: operations["getPosts"];
  };
}

export type webhooks = Record<string, never>;

export interface components {
  schemas: {
    GetPostsResponseBody: {
      items?: components["schemas"]["PostDto"][];
    };
    PostDto: {
      /** Format: int64 */
      id?: number;
      /** Format: date-time */
      createDate?: string;
      /** Format: date-time */
      modifyDate?: string;
      /** Format: int64 */
      authorId?: number;
      authorName?: string;
      title?: string;
      body?: string;
    };
    RsDataGetPostsResponseBody: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["GetPostsResponseBody"];
      success: boolean;
      fail: boolean;
    };
  };
  responses: never;
  parameters: never;
  requestBodies: never;
  headers: never;
  pathItems: never;
}

export type $defs = Record<string, never>;

export type external = Record<string, never>;

export interface operations {

  getPosts: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "*/*": components["schemas"]["RsDataGetPostsResponseBody"];
        };
      };
    };
  };
}